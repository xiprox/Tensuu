package tr.xip.scd.tensuu.ui.common

interface AnimateableFragment {
    fun animateEnter()
    fun animateExit()
    fun getEnterDuration(): Long
    fun getExitDuration(): Long
}