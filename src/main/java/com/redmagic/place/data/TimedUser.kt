package com.redmagic.place.data

import com.redmagic.place.PlaceMC
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

class TimedUser(
    val uuid: UUID,
    var startTime: LocalDateTime?,
    var amount: Int
) {

    fun secondsLeft(): Int {
        val now = LocalDateTime.now()
        startTime?.let {
            val doneTime = it.plusMinutes(1)
            if (doneTime.isBefore(now)) {
                startTime = null
                PlaceMC.placeMC.actionBarLoop.actionPlayers.remove(uuid)
                return 0
            }
            return Duration.between(now, doneTime).toSeconds().toInt()
        }
        return 0
    }
}