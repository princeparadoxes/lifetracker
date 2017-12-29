/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public class Color {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected Color(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Color obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_Color(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public Color() {
    this(liquidfunJNI.new_Color__SWIG_0(), true);
  }

  public Color(float r, float g, float b) {
    this(liquidfunJNI.new_Color__SWIG_1(r, g, b), true);
  }

  public void set(float ri, float gi, float bi) {
    liquidfunJNI.Color_set(swigCPtr, this, ri, gi, bi);
  }

  public void setR(float value) {
    liquidfunJNI.Color_r_set(swigCPtr, this, value);
  }

  public float getR() {
    return liquidfunJNI.Color_r_get(swigCPtr, this);
  }

  public void setG(float value) {
    liquidfunJNI.Color_g_set(swigCPtr, this, value);
  }

  public float getG() {
    return liquidfunJNI.Color_g_get(swigCPtr, this);
  }

  public void setB(float value) {
    liquidfunJNI.Color_b_set(swigCPtr, this, value);
  }

  public float getB() {
    return liquidfunJNI.Color_b_get(swigCPtr, this);
  }

}
