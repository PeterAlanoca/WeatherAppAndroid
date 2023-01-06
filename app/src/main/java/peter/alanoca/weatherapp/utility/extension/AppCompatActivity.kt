package peter.alanoca.weatherapp.utility.extension

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import peter.alanoca.weatherapp.R
import peter.alanoca.weatherapp.utility.Constants

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.push(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
            R.anim.anim_fragment_slide_left_enter,
            R.anim.anim_fragment_slide_left_exit,
            R.anim.anim_fragment_slide_right_enter,
            R.anim.anim_fragment_slide_right_exit
        )
        .replace(R.id.frameLayout, fragment, Constants.FRAGMENT_DETAIL)
        .addToBackStack(null)
        .commitAllowingStateLoss()
}

fun AppCompatActivity.back() {
    if (supportFragmentManager.backStackEntryCount > 0) {
        supportFragmentManager.popBackStack()
    } else {
        finish()
    }
}

fun AppCompatActivity.showBottomSheet(dialogFragment: DialogFragment) {
    hideBottomSheet()
    dialogFragment.show(supportFragmentManager, Constants.TAG_BOTTOM_SHEET)
    this.dialogFragment = dialogFragment
}

fun AppCompatActivity.hideBottomSheet() {
    dialogFragment?.dismiss()
}


fun AppCompatActivity.showDialog(dialogFragment: DialogFragment) {
    hideDialog()
    dialogFragment.show(supportFragmentManager, Constants.TAG_BOTTOM_SHEET)
    this.dialogFragment = dialogFragment
}

fun AppCompatActivity.hideDialog() {
    dialogFragment?.dismiss()
}

var currentDialogFragment: DialogFragment? = null

var AppCompatActivity.dialogFragment: DialogFragment?
    get() = currentDialogFragment
    set(value: DialogFragment?) { currentDialogFragment = value }

