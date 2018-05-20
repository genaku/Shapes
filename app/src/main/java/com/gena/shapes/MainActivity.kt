package com.gena.shapes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.gena.domain.consts.ShapeType
import com.gena.domain.model.MenuCommand
import com.gena.domain.model.ShapesModel
import com.gena.domain.usecases.interfaces.IInteractor
import com.gena.shapes.extensions.getViewModel
import com.gena.shapes.extensions.setObserver
import kotlinx.android.synthetic.main.activity_main.*
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

    private lateinit var mInteractor: IInteractor
    private var mCenterX: Int = 0
    private var mCenterY: Int = 0
    private lateinit var mViewModel: ShapesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        mViewModel = getViewModel { ShapesViewModel(application, ShapesApplication.repository) }.apply {
            actionsAvailability.setObserver(this@MainActivity, { value -> value?.apply { invalidateOptionsMenu() } })
            shapesToRefresh.setObserver(this@MainActivity, { value -> redrawShapes(value) })
        }
        mInteractor = mViewModel.interactor
        viewPanel.setInteractor(mInteractor)
    }

    private fun redrawShapes(model: ShapesModel?) {
        model ?: return
        viewPanel.setModel(model)
        viewPanel.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mInteractor.startObserving()
    }

    override fun onPause() {
        mInteractor.stopObserving()
        mInteractor.saveShapes()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_shapes, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu ?: return super.onPrepareOptionsMenu(menu)
        var result = false
        mViewModel.actionsAvailability.value?.forEach { (menuCommand, available) ->
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
        R.id.action_picture -> execMenuCommand {
            getPicture()
//            addShape(ShapeType.PICTURE)
        }
        R.id.action_undo -> execMenuCommand { mInteractor.undo() }
        R.id.action_redo -> execMenuCommand { mInteractor.redo() }
        R.id.action_delete -> execMenuCommand { mInteractor.deleteSelected() }
        else -> super.onOptionsItemSelected(item)
    }

    private fun execMenuCommand(command: () -> Unit): Boolean {
        command()
        return true
    }

    private fun addShape(type: ShapeType) =
            mInteractor.addShape(type, mCenterX, mCenterY)

    private fun getPicture() {
        EasyImage.openGallery(this, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                onPhotosReturned(imageFile)
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
                e!!.printStackTrace()
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
        mInteractor.addPicture(mCenterX, mCenterY, returnedPhoto.absolutePath)
    }

    override fun onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(this)
        super.onDestroy()
    }

}
