package besmart.elderlycare.model.villageitem

interface VillageType {

    var type: Int

    companion object {
        const val TILTE = 1
        const val HEAD = 2
        const val LIST = 3
    }
}