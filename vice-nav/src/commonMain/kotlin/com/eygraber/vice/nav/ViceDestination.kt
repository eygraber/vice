package com.eygraber.vice.nav

import com.eygraber.vice.ViceCompositor
import com.eygraber.vice.ViceContainer
import com.eygraber.vice.ViceEffects

public abstract class ViceDestination<I, C, E, S> : ViceContainer<I, C, E, S>()
  where C : ViceCompositor<I, S>, I : Any, E : ViceEffects, S : Any
