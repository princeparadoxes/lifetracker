/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public class QueryCallback {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected QueryCallback(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(QueryCallback obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_QueryCallback(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    liquidfunJNI.QueryCallback_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    liquidfunJNI.QueryCallback_change_ownership(this, swigCPtr, true);
  }

  public QueryCallback() {
    this(liquidfunJNI.new_QueryCallback(), true);
    liquidfunJNI.QueryCallback_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

  public boolean reportFixture(Fixture fixture) {
    return liquidfunJNI.QueryCallback_reportFixture(swigCPtr, this, Fixture.getCPtr(fixture), fixture);
  }

  public boolean reportParticle(ParticleSystem particleSystem, int index) {
    return (getClass() == QueryCallback.class) ? liquidfunJNI.QueryCallback_reportParticle(swigCPtr, this, ParticleSystem.getCPtr(particleSystem), particleSystem, index) : liquidfunJNI.QueryCallback_reportParticleSwigExplicitQueryCallback(swigCPtr, this, ParticleSystem.getCPtr(particleSystem), particleSystem, index);
  }

}
