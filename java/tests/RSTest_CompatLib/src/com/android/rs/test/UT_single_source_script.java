/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.rs.test_compat;

import android.content.Context;
import android.content.res.Resources;
import android.support.v8.renderscript.*;

public class UT_single_source_script extends UnitTest {
    private Resources mRes;
    private Allocation testAllocation1, testAllocation2;

    protected UT_single_source_script(RSTestCore rstc, Resources res, Context ctx) {
        super(rstc, "SingleSourceScript", ctx);
        mRes = res;
    }

    private void initializeGlobals(RenderScript RS, ScriptC_single_source_script s) {
        Type.Builder i32TypeBuilder = new Type.Builder(RS, Element.I32(RS));
        int X = 1024;
        int Y = 768;
        s.set_dimX(X);
        s.set_dimY(Y);
        i32TypeBuilder.setX(X).setY(Y);
        testAllocation1 = Allocation.createTyped(RS, i32TypeBuilder.create());
        testAllocation2 = Allocation.createTyped(RS, i32TypeBuilder.create());
    }

    public void run() {
        RenderScript pRS = RenderScript.create(mCtx);
        ScriptC_single_source_script s = new ScriptC_single_source_script(pRS);
        pRS.setMessageHandler(mRsMessage);
        initializeGlobals(pRS, s);

        s.invoke_entrypoint(testAllocation1, testAllocation2);
        s.forEach_oldFoo(testAllocation2, testAllocation2);
        s.forEach_oldGoo(testAllocation2, testAllocation2);
        s.invoke_validate(testAllocation2);

        pRS.finish();
        waitForMessage();
        pRS.destroy();
    }
}
