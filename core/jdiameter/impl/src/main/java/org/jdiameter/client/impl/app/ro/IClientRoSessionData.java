/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jdiameter.client.impl.app.ro;

import org.jdiameter.api.Request;
import org.jdiameter.common.api.app.ro.ClientRoSessionState;
import org.jdiameter.common.api.app.ro.IRoSessionData;

import java.io.Serializable;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
*  @author <a href="mailto:grzegorz.figiel@pro-ids.com"> Grzegorz Figiel (ProIDS sp. z o.o.)</a>
 */
public interface IClientRoSessionData extends IRoSessionData {

  public boolean isEventBased();

  public void setEventBased(boolean b);

  public boolean isRequestTypeSet();

  public void setRequestTypeSet(boolean b);

  public ClientRoSessionState getClientRoSessionState();

  public void setClientRoSessionState(ClientRoSessionState state);

  public Serializable getTxTimerId();

  public void setTxTimerId(Serializable txTimerId);

  public Request getTxTimerRequest();

  public void setTxTimerRequest(Request txTimerRequest);
  
  public Serializable getRetransmissionTimerId();

  public void setRetransmissionTimerId(Serializable retransmissionTimerId);

  public Request getBuffer();

  public void setBuffer(Request buffer);

  public int getGatheredRequestedAction();

  public void setGatheredRequestedAction(int gatheredRequestedAction);

  public int getGatheredCCFH();

  public void setGatheredCCFH(int gatheredCCFH);

  public int getGatheredDDFH();

  public void setGatheredDDFH(int gatheredDDFH);

  public int getGatheredCCSF();

  public void setGatheredCCSF(int gatheredCCSF);

}
