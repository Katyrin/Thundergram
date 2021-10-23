package com.katyrin.thundergram.view.inputvalidators

import android.text.Editable
import android.text.TextWatcher
import java.util.regex.Pattern

class PhoneNumberValidator : TextWatcher {

    internal var isValid = false

    override fun afterTextChanged(editableText: Editable) {
        isValid = isValidPhoneNumber(editableText)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit

    companion object {
        private val PHONE_PATTERN = Pattern.compile(
            "[+](((55|86)[0-9]{11}$)|" +
                    "((1|7|20|30|41|44|57|58|63|81|90|91|92|98|378|856|964|977)[0-9]{10}$)|" +
                    "((27|31|32|33|34|36|40|46|48|51|56|61|66|93|94|211|212|213|218|221|224|240|" +
                    "242|243|244|249|250|251|254|255|256|257|258|260|261|262|263|264|265|351|353|" +
                    "355|375|380|381|420|421|590|593|595|886|962|963|966|967|970|971|972|992|994|" +
                    "995|996|997|998)[0-9]{9}$)|" +
                    "((45|47|53|65|216|222|223|225|226|227|228|229|232|235|236|237|252|253|266|" +
                    "267|268|350|356|357|370|371|373|374|377|383|386|387|389|502|503|504|505|506|" +
                    "507|591|598|852|853|965|968|973|974|975|976|993)[0-9]{8}$)|" +
                    "((220|238|239|241|245|246|248|269|291|297|354|597|673|860|1242|1246|1264|" +
                    "1268|1284|1340|1345|1441|1473|1649|1664|1670|1671|1684|1721|1758|1767|1784|" +
                    "1868|1869|1876)[0-9]{7}$)|" +
                    "((298|299|376)[0-9]{6}$)|" +
                    "((290)[0-9]{5}$)|" +
                    "((247)[0-9]{4}$)|" +
                    "((39|42|43|49|52|54|60|62|64|82|84|95|230|231|233|234|352|358|359|372|382|" +
                    "385|423|500|501|508|509|592|594|596|599|670|672|674|675|676|677|678|679|" +
                    "680|681|682|683|685|686|687|688|689|690|691|692|850|855|880|881|882|883|" +
                    "861)[0-9]{5,}))"
        )
        fun isValidPhoneNumber(phoneNumber: CharSequence?): Boolean =
            phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches()
    }
}