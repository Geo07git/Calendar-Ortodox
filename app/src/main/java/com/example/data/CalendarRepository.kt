package com.example.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class OrthodoxDay(
    val month: Int,
    val day: Int,
    val title: String,
    val isRedCross: Boolean = false,
    val isBlackCross: Boolean = false,
    val fastingType: FastingType = FastingType.NONE,
    val synaxarion: String = "Sfinții zilei au apărat dreapta credință și au suferit martiriul pentru Hristos. Aceștia ne sunt modele de viețuire creștină, arătându-ne calea către mântuire și iubirea desăvârșită.",
    val meaning: String = "Această zi este un prilej de rugăciune și reculegere. Credincioșii sunt chemați să participe la Sfânta Liturghie și să facă fapte de milostenie, urmând exemplul sfinților pomeniți."
)

enum class FastingType(val description: String) {
    NONE("Nu este post"),
    FAST("Post"),
    FISH_ALLOWED("Dezlegare la pește"),
    OIL_WINE_ALLOWED("Dezlegare la ulei și vin"),
    DAIRY_ALLOWED("Harți (dezlegare la brânză, lapte, ouă)")
}

class CalendarRepository {
    // Static mock data for demonstration. 
    // In a real application, this would use a Room database populated from a web API.
    private val holidays = listOf(
        OrthodoxDay(6, 24, "Nașterea Sf. Proroc Ioan Botezătorul (Sânzienele)", isRedCross = true, fastingType = FastingType.FISH_ALLOWED, synaxarion = "Nașterea Sfântului Proroc Ioan Botezătorul (Sânzienele, Drăgaica). Sfântul Ioan Botezătorul a fost Înaintemergătorul Domnului, pregătind calea pentru venirea lui Hristos. S-a născut din părinții Zaharia și Elisabeta, la bătrânețe.", meaning = "Este o sărbătoare mare a bucuriei nașterii Celui care a botezat pe Hristos. Tradiția populară asociază această zi cu Sânzienele, o sărbătoare a soarelui și a verii."),
        OrthodoxDay(6, 28, "Aducerea moaștelor Sf. Mc. Chir și Ioan", fastingType = FastingType.NONE, synaxarion = "Sfinții Mucenici Chir și Ioan au suferit pentru credința lor în Hristos. Sfinții doctori fără de arginți Chir și Ioan au tămăduit multe boli, atât trupești cât și sufletești, doar prin rugăciune.", meaning = "Pomenirea lor ne amintește de datoria de a avea milă față de cei bolnavi și suferinzi."),
        OrthodoxDay(6, 29, "Sfinții Apostoli Petru și Pavel", isRedCross = true, fastingType = FastingType.FISH_ALLOWED, synaxarion = "Sfinții Apostoli Petru și Pavel sunt considerați căpeteniile apostolilor. Sfântul Petru a predicat la Roma și a fost răstignit cu capul în jos, iar Sfântul Pavel a fost apostolul neamurilor, martirizat prin tăierea capului la Roma.", meaning = "Această zi marchează sfârșitul postului Sfinților Apostoli și sărbătorește râvna misionară și martiriul celor mai mari apostoli ai creștinătății."),
        OrthodoxDay(6, 30, "Soborul Sfinților 12 Apostoli", isBlackCross = true, fastingType = FastingType.NONE, synaxarion = "Soborul Sfinților celor Doisprezece Apostoli. Biserica îi cinstește împreună pe toți cei 12 Apostoli ai lui Hristos: Petru, Andrei, Iacob, Ioan, Filip, Vartolomeu, Toma, Matei, Iacob al lui Alfeu, Iuda Tadeul, Simon Zilotul și Matia.", meaning = "Această sărbătoare ne aduce aminte de chemarea Bisericii de a fi apostolică și de a răspândi Evanghelia."),
        OrthodoxDay(7, 1, "Sf. Ierarh Leontie de la Rădăuți; Sf. Cosma și Damian", isBlackCross = true, fastingType = FastingType.FAST, synaxarion = "Sfântul Ierarh Leontie a fost episcop de Rădăuți și un mare duhovnic al Moldovei. Sfinții Cosma și Damian au fost frați și medici fără de arginți, vindecând bolnavii gratuit.", meaning = "Pilda sfinților doctori fără de arginți ne învață generozitatea și iubirea dezinteresată față de semeni."),
        OrthodoxDay(7, 2, "Așezarea veșmântului Născătoarei de Dumnezeu; Sf. Ștefan cel Mare", isBlackCross = true, fastingType = FastingType.NONE, synaxarion = "Sărbătoarea așezării veșmântului Maicii Domnului în Biserica Vlaherne din Constantinopol. Tot azi, Biserica Ortodoxă Română îl cinstește pe Sfântul Voievod Ștefan cel Mare, apărător al creștinătății.", meaning = "Sărbătoarea ne amintește de ocrotirea permanentă a Maicii Domnului și de modelul de conducător creștin lăsat de Ștefan cel Mare."),
        OrthodoxDay(7, 20, "Sfântul Slăvitul Proroc Ilie Tesviteanul", isRedCross = true, fastingType = FastingType.NONE, synaxarion = "Sfântul Proroc Ilie a trăit în timpul regelui Ahab. A mustrat idolatria și a săvârșit multe minuni, inclusiv închiderea cerului și aducerea ploii, și învierea fiului văduvei din Sarepta.", meaning = "Prorocul Ilie este un model de curaj și zel pentru dreapta credință. Tradiția populară îl asociază cu ploile și fulgerele."),
        OrthodoxDay(8, 6, "Schimbarea la Față a Domnului", isRedCross = true, fastingType = FastingType.FISH_ALLOWED, synaxarion = "Pe muntele Tabor, Hristos S-a schimbat la față înaintea ucenicilor Săi Petru, Iacob și Ioan, arătându-Și slava dumnezeiască. Au apărut Moise și Ilie vorbind cu El.", meaning = "Schimbarea la Față ne descoperă chemarea omului la îndumnezeire și lumina necreată a lui Dumnezeu."),
        OrthodoxDay(8, 15, "Adormirea Maicii Domnului", isRedCross = true, fastingType = FastingType.NONE, synaxarion = "Adormirea Maicii Domnului și mutarea ei la cer cu trupul. Apostolii s-au adunat în chip minunat la Ierusalim pentru a îngropa trupul Preasfintei Născătoare de Dumnezeu.", meaning = "Cea mai mare sărbătoare închinată Maicii Domnului, numită și Paștele verii. Este un moment de speranță în viața veșnică."),
    )

    fun getDayInfo(localDate: LocalDate): OrthodoxDay {
        return holidays.find { it.month == localDate.monthValue && it.day == localDate.dayOfMonth }
            ?: OrthodoxDay(
                month = localDate.monthValue,
                day = localDate.dayOfMonth,
                title = "Sfântul zilei (Date indisponibile offline)",
                fastingType = calculateGeneralFasting(localDate)
            )
    }
    
    fun getUpcomingDays(startDate: LocalDate, days: Int): List<Pair<LocalDate, OrthodoxDay>> {
        val result = mutableListOf<Pair<LocalDate, OrthodoxDay>>()
        for (i in 0 until days) {
            val date = startDate.plusDays(i.toLong())
            result.add(Pair(date, getDayInfo(date)))
        }
        return result
    }

    private fun calculateGeneralFasting(date: LocalDate): FastingType {
        // Wednesday and Friday are generally fast days
        return if (date.dayOfWeek.value == 3 || date.dayOfWeek.value == 5) {
            FastingType.FAST
        } else {
            FastingType.NONE
        }
    }
}
