package com.example.diploma.presentation.ui.tabs.manager.detailsManager

import com.example.diploma.domain.models.Consumer
import com.example.diploma.domain.models.Producer
import com.example.diploma.domain.models.Transport

sealed class MembersEvent{
    data class AddProducerItem(val producer: Producer):MembersEvent()
    data class AddConsumerItem(val consumer:Consumer):MembersEvent()
    data class DeleteConsumerItem(val consumer:Consumer):MembersEvent()
    data class DeleteProducerItem(val producer: Producer):MembersEvent()
    data class UpdateConsumerItem(val consumer:Consumer):MembersEvent()
    data class UpdateProducerItem(val producer: Producer):MembersEvent()
    data class AddTransportItem(val transport: Transport):MembersEvent()
    data class DeleteTransportItem(val transport: Transport):MembersEvent()
    data class UpdateTransportItem(val transport: Transport):MembersEvent()
}

sealed class DialogEvent{
    data object OpenConsumerDialog:DialogEvent()
    data object OpenProducerDialog:DialogEvent()
    data object HideMembersDialog:DialogEvent()
    data object HideTransportDialog:DialogEvent()
    data class OpenTransportDialog(val transport: Transport?) : DialogEvent()
}


