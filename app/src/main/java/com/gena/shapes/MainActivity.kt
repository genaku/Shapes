package com.gena.shapes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.gena.domain.consts.ShapeError
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.MenuCommand
import com.gena.domain.model.ShapesModel
import com.gena.domain.usecases.interfaces.IShapesHistoryInteractor
import com.gena.shapes.extensions.getViewModel
import com.gena.shapes.extensions.setObserver
import com.gena.shapes.viewmodel.ErrorViewModel
import com.gena.shapes.viewmodel.MenuViewModel
import com.gena.shapes.viewmodel.ShapesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.tajchert.nammu.Nammu
import pl.tajchert.nammu.PermissionCallback
import java.io.File

/**
 * Created by Gena Kuchergin on 03.02.2018.
 * © 2018 Gena Kuchergin. All Rights Reserved.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mShapesViewModel: ShapesViewModel
    private lateinit var mMenuViewModel: MenuViewModel
    private lateinit var mShapesHistoryInteractor: IShapesHistoryInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Nammu.init(applicationContext)
        setupView()

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionCallback {
                override fun permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                override fun permissionRefused() {
//                    finish()
                }
            })
        }
    }

    private fun setupView() {
        mMenuViewModel = getViewModel { MenuViewModel() }.apply {
            actionsAvailability.setObserver(this@MainActivity) { value -> value?.apply { invalidateOptionsMenu() } }
        }
        val errorViewModel = getViewModel { ErrorViewModel() }.apply {
            errorEvent.setObserver(this@MainActivity) { value -> showErrorToast(value) }
        }
        mShapesViewModel = getViewModel { ShapesViewModel(ShapesApplication.repository, mMenuViewModel.presenter, errorViewModel.presenter) }.apply {
            shapesToRefresh.setObserver(this@MainActivity) { value -> redrawShapes(value) }
        }
        mShapesHistoryInteractor = mShapesViewModel.shapesHistoryInteractor
        viewPanel.setInteractor(mShapesViewModel.selectionInteractor)
    }

    private fun redrawShapes(model: ShapesModel?) {
        model ?: return
        viewPanel.setModel(model)
        viewPanel.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mShapesHistoryInteractor.startObserving()
    }

    override fun onPause() {
        mShapesHistoryInteractor.stopObserving()
        mShapesHistoryInteractor.saveShapes()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_shapes, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu ?: return super.onPrepareOptionsMenu(menu)
        var result = false
        mMenuViewModel.actionsAvailability.value?.forEach { (menuCommand, available) ->
            menuCommand?.apply {
                result = menu.setMenuItemAvailability(this.toResId(), available)
            }
        }
        return if (!result) super.onPrepareOptionsMenu(menu) else true
    }

    private fun MenuCommand.toResId(): Int = when (this) {
        MenuCommand.ADD_TRIANGLE -> R.id.action_triangle
        MenuCommand.ADD_RECTANGLE -> R.id.action_rectangle
        MenuCommand.ADD_OVAL -> R.id.action_oval
        MenuCommand.ADD_PICTURE -> R.id.action_picture
        MenuCommand.REDO -> R.id.action_redo
        MenuCommand.UNDO -> R.id.action_undo
        MenuCommand.DELETE -> R.id.action_delete
    }

    private fun Menu.setMenuItemAvailability(resId: Int, available: Boolean): Boolean {
        this.findItem(resId)?.apply {
            isEnabled = available
            icon.alpha = if (available) 255 else 130
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.action_triangle -> execMenuCommand { addShape(ShapeType.TRIANGLE) }
        R.id.action_rectangle -> execMenuCommand { addShape(ShapeType.RECTANGLE) }
        R.id.action_oval -> execMenuCommand { addShape(ShapeType.OVAL) }
        R.id.action_picture -> execMenuCommand { getPicture() }
        R.id.action_undo -> execMenuCommand { mShapesHistoryInteractor.undo() }
        R.id.action_redo -> execMenuCommand { mShapesHistoryInteractor.redo() }
        R.id.action_delete -> execMenuCommand { mShapesHistoryInteractor.deleteSelected() }
        else -> super.onOptionsItemSelected(item)
    }

    private fun execMenuCommand(command: () -> Unit): Boolean {
        command()
        return true
    }

    private fun addShape(type: ShapeType) =
            mShapesHistoryInteractor.addShape(type)

    private fun getPicture() {
        EasyImage.openGallery(this, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                onPhotosReturned(imageFile)
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                e?.printStackTrace()
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(this@MainActivity)
                    photoFile?.delete()
                }
            }
        })
    }

    private fun onPhotosReturned(returnedPhoto: File?) {
        returnedPhoto ?: return
        mShapesHistoryInteractor.addPicture(returnedPhoto.absolutePath)
    }

    private fun showErrorToast(error: ShapeError?) {
        error ?: return
        toast(when (error) {
            ShapeError.CANT_UNDO -> getString(R.string.cant_undo)
            ShapeError.CANT_REDO -> getString(R.string.cant_redo)
            ShapeError.CANT_EXEC_COMMAND -> getString(R.string.cant_exec)
            ShapeError.CANT_SAVE_TO_HISTORY -> getString(R.string.cant_history)
        })
    }

    override fun onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(this)
        super.onDestroy()
    }

}
