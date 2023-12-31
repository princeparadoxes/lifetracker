/**
 * Copyright (c) 2014 Google, Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.fpl.liquidfunpaint.shader;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.google.fpl.liquidfunpaint.physics.ParticleSystems;
import com.google.fpl.liquidfunpaint.physics.WorldLock;
import com.google.fpl.liquidfunpaint.renderer.ParticleRenderer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ParticleMaterial.
 * This is the particle point sprite material.
 */
public class ParticleMaterial extends Material {
    private static final String TAG = "ParticleMaterial";
    private static final String DIFFUSE_TEXTURE_NAME = "uDiffuseTexture";

    private float mParticleSizeScale;

    public ParticleMaterial(Context context, JSONObject json) {
        super(new ShaderProgram("particle.glslv", "particle.glslf"));

        // Read in values from the JSON file
        mParticleSizeScale =
                (float) json.optDouble("particleSizeScale", 1.0);

        // Add the water texture that is scrolling
        try {
            String textureName = json.getString(DIFFUSE_TEXTURE_NAME);
            addTexture(DIFFUSE_TEXTURE_NAME, new Texture(context, textureName));
        } catch (JSONException ex) {
            Log.e(TAG, "Missing point sprite texture!\n" + ex.getMessage());
        }
    }

    @Override
    public void beginRender() {
        super.beginRender();

        float pSize = mParticleSizeScale * ParticleRenderer.FB_SIZE *
                (ParticleSystems.PARTICLE_RADIUS /
                        Math.min(WorldLock.getInstance().sRenderWorldWidth, WorldLock.getInstance().sRenderWorldHeight));

        // Specific uniforms to this material
        GLES20.glUniform1f(
                getUniformLocation("uPointSize"),
                Math.max(1.0f, pSize));
    }
}
