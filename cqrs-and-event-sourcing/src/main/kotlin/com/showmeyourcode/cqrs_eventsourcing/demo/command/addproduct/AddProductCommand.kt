package com.showmeyourcode.cqrs_eventsourcing.demo.command.addproduct

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.Command
import lombok.Getter
import lombok.Setter

@Getter
@Setter
class AddProductCommand(val name: String, val availability: Int) :
    Command<AddProductCommandResult>
