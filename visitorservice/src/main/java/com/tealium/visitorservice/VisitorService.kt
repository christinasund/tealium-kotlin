package com.tealium.visitorservice

import com.tealium.core.*

/**
 * The VisitorService module is responsible for fetching updates to a VisitorProfile, and notifying
 * delegates that there have been updates.
 */
class VisitorService(context: TealiumContext) : Module {

    override val name: String = MODULE_NAME
    override var enabled: Boolean = true

    private val visitorProfileManager = VisitorProfileManager(context)

    init {
        context.events.subscribe(visitorProfileManager)
    }

    val visitorProfile: VisitorProfile
        get() = visitorProfileManager.visitorProfile

    /**
     * Retrieves the latest VisitorProfile. If there are updates then the delegate will be informed.
     */
    suspend fun requestVisitorProfile() = visitorProfileManager.requestVisitorProfile()

    companion object : ModuleFactory {
        const val MODULE_NAME = "VISITOR_SERVICE"

        override fun create(context: TealiumContext): Module {
            return VisitorService(context)
        }
    }
}

val Modules.VisitorService: ModuleFactory
    get() = com.tealium.visitorservice.VisitorService

/**
 * Returns the VisitorService module for this Tealium instance.
 */
val Tealium.visitorService: VisitorService?
    get() = modules.getModule(VisitorService.MODULE_NAME) as? VisitorService