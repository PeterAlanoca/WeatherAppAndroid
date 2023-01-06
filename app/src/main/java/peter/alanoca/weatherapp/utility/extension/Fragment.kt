package peter.alanoca.weatherapp.utility.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) {
    (activity as AppCompatActivity).showToast(message)
}

fun Fragment.push(fragment: Fragment) {
    (activity as AppCompatActivity).push(fragment)
}

fun Fragment.back() {
    (activity as AppCompatActivity).back()
}

fun Fragment.showBottomSheet(dialogFragment: DialogFragment) {
    (activity as AppCompatActivity).showBottomSheet(dialogFragment)
}

fun Fragment.hideBottomSheet() {
    (activity as AppCompatActivity).hideBottomSheet()
}

fun Fragment.showDialog(dialogFragment: DialogFragment) {
    (activity as AppCompatActivity).showBottomSheet(dialogFragment)
}

fun Fragment.hideDialog() {
    (activity as AppCompatActivity).hideBottomSheet()
}
