package com.showmeyourcode.cqrs.demo.command.changeavailability

import com.showmeyourcode.cqrs.demo.infra.Command
import java.util.*

class ChangeProductAvailabilityCommand(val id: UUID, val newAvailability: Int) : Command<Unit>
