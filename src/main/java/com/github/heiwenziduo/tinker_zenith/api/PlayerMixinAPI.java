package com.github.heiwenziduo.tinker_zenith.api;


public interface PlayerMixinAPI {
    // it's public static final, we don't want static
    // FlyingSword[] nineSword$HotbarHolder = new FlyingSword[9];

    // still not work
    // FlyingSword[] getNineSword$HotbarHolder();

    // done
    FlyingSwordManager getFlyingSwordManager();
}
