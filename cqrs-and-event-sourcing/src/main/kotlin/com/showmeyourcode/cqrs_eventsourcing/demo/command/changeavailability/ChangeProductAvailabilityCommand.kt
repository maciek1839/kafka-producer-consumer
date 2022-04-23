package com.showmeyourcode.cqrs_eventsourcing.demo.command.changeavailability

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.Command
import java.util.*

class ChangeProductAvailabilityCommand(val id: UUID, val newAvailability: Int) :
    Command<Unit>
