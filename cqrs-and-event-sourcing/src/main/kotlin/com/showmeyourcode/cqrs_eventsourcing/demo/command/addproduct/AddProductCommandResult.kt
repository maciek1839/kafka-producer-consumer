package com.showmeyourcode.cqrs_eventsourcing.demo.command.addproduct

import lombok.Getter
import lombok.Setter
import java.util.*

@Getter
@Setter
class AddProductCommandResult(val id: UUID)
