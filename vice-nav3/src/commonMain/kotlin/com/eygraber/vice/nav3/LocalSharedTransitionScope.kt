@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.eygraber.vice.nav3

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.OverlayClip
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize.Companion.contentSize
import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.ScaleToBounds
import androidx.compose.animation.SharedTransitionScope.SharedContentState
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

public val LocalSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
  compositionLocalOf { error("LocalSharedTransitionScope not provided") }

@Composable
public fun rememberSharedContentState(
  key: Any,
): SharedContentState =
  with(LocalSharedTransitionScope.current) {
    rememberSharedContentState(key)
  }

public fun Modifier.sharedElement(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  state: SharedContentState,
  boundsTransform: BoundsTransform = DefaultBoundsTransform,
  placeHolderSize: SharedTransitionScope.PlaceHolderSize = contentSize,
  renderInOverlayDuringTransition: Boolean = true,
  zIndexInOverlay: Float = 0f,
  clipInOverlayDuringTransition: OverlayClip = ParentClip,
): Modifier = with(sharedTransitionScope) {
  this@sharedElement.sharedElement(
    sharedContentState = state,
    animatedVisibilityScope = animatedVisibilityScope,
    boundsTransform = boundsTransform,
    placeHolderSize = placeHolderSize,
    renderInOverlayDuringTransition = renderInOverlayDuringTransition,
    zIndexInOverlay = zIndexInOverlay,
    clipInOverlayDuringTransition = clipInOverlayDuringTransition,
  )
}

public fun Modifier.sharedBounds(
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  enter: EnterTransition = fadeIn(),
  exit: ExitTransition = fadeOut(),
  sharedContentState: SharedContentState,
  boundsTransform: BoundsTransform = DefaultBoundsTransform,
  resizeMode: ResizeMode = ScaleToBounds(ContentScale.FillWidth, Center),
  placeHolderSize: SharedTransitionScope.PlaceHolderSize = contentSize,
  renderInOverlayDuringTransition: Boolean = true,
  zIndexInOverlay: Float = 0f,
  clipInOverlayDuringTransition: OverlayClip = ParentClip,
): Modifier = with(sharedTransitionScope) {
  this@sharedBounds.sharedBounds(
    sharedContentState = sharedContentState,
    animatedVisibilityScope = animatedVisibilityScope,
    enter = enter,
    exit = exit,
    boundsTransform = boundsTransform,
    resizeMode = resizeMode,
    placeHolderSize = placeHolderSize,
    renderInOverlayDuringTransition = renderInOverlayDuringTransition,
    zIndexInOverlay = zIndexInOverlay,
    clipInOverlayDuringTransition = clipInOverlayDuringTransition,
  )
}

// the following are currently private in CMP
private val DefaultSpring = spring(
  stiffness = StiffnessMediumLow,
  visibilityThreshold = Rect.VisibilityThreshold,
)

@ExperimentalSharedTransitionApi
private val ParentClip: OverlayClip =
  object : OverlayClip {
    override fun getClipPath(
      sharedContentState: SharedContentState,
      bounds: Rect,
      layoutDirection: LayoutDirection,
      density: Density,
    ): Path? = sharedContentState.parentSharedContentState?.clipPathInOverlay
  }

@ExperimentalSharedTransitionApi
private val DefaultBoundsTransform = BoundsTransform { _, _ -> DefaultSpring }
