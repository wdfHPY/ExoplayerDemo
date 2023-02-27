# ExoplayerDemo

use Google Exoplayer to realize Tiktok-like short video Demo

# The basic idea:

1. Create a container to store Exoplayer. In the demo, SparseArray is used to store Exoplayer. In
   the Adapter's onBindViewHolder method, initialize the Exoplayer object and add it to the
   SparseArray.

```kotlin
//SparseArray for store ExoPlayer
private val mStoredVideoPlayers: SparseArray<ExoPlayer> = SparseArray<ExoPlayer>()

//SparseArray for store StyledPlayerView
private val mStoredVideoView: SparseArray<StyledPlayerView> = SparseArray<StyledPlayerView>()
```

2. When ViewPager2 slides in, onViewAttachedToWindow will be called, and the Exoplayer can be
   obtained through Position and then prepare the exoplayer object.(For details, please refer to the usage in Adapter.)

3. When ViewPager2 slides out, onViewDetachedFromWindow will be called, and then use the Exoplayer
   object to call the stop method.(For details, please refer to the usage in Adapter.)