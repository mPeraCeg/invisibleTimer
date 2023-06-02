package com.example.invisibletimer

class DatabaseUtils {

    val mainUtils = MainUtils()

    fun insertTimer(dbHelper: MyDatabaseHelper, lastId: Int, date: String, start_time: String) {
        dbHelper.insertTimer(Timer(
            id = lastId+1,
            date = date,
            start_time = start_time,
            end_time = "",
            total = 0.0))
    }

    fun updateDateTimer(dbHelper: MyDatabaseHelper, timer: Timer, endTime: String, totalTime: Double) {
        dbHelper.updateTimer(Timer(
            id = timer.id,
            date = timer.date,
            start_time = timer.start_time,
            end_time = endTime,
            total = totalTime))
    }

    fun getHistoryTimers(dbHelper: MyDatabaseHelper): List<HistoryActivity.HistoricEntry> {
        val listOfTimers = dbHelper.getAllTimers()

        val dictOfTimers = HashMap<String, Double>()
        for (timer in listOfTimers) {
            var date = timer.date
            if (dictOfTimers.containsKey(date)) {
                val newTotalTimer = timer.total + dictOfTimers.get(date)!!
                dictOfTimers.put(date, newTotalTimer)
            } else {
                dictOfTimers.put(date, timer.total)
            }
        }

        val listOfHistoricEntry = ArrayList<HistoryActivity.HistoricEntry>()
        val sortedDictOfTimers = dictOfTimers.toSortedMap()
        sortedDictOfTimers.forEach{ entry ->
            val totalTimerString = mainUtils.secondsToReadableFormat(entry.value, false)
            listOfHistoricEntry.add(HistoryActivity.HistoricEntry(
                entry.key, totalTimerString, mainUtils.getStatusDrawable(totalTimerString)))
        }
        return listOfHistoricEntry
    }

}