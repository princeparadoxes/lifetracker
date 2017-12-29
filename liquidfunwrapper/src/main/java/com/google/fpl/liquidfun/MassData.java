/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public class MassData {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected MassData(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(MassData obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_MassData(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setMass(float value) {
    liquidfunJNI.MassData_mass_set(swigCPtr, this, value);
  }

  public float getMass() {
    return liquidfunJNI.MassData_mass_get(swigCPtr, this);
  }

  public void setCenter(Vec2 value) {
    liquidfunJNI.MassData_center_set(swigCPtr, this, Vec2.getCPtr(value), value);
  }

  public Vec2 getCenter() {
    return new Vec2(liquidfunJNI.MassData_center_get(swigCPtr, this), false);
  }

  public void setI(float value) {
    liquidfunJNI.MassData_I_set(swigCPtr, this, value);
  }

  public float getI() {
    return liquidfunJNI.MassData_I_get(swigCPtr, this);
  }

}
