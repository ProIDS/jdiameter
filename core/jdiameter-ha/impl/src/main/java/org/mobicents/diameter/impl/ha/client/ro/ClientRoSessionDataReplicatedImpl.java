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

package org.mobicents.diameter.impl.ha.client.ro;

import org.jboss.cache.Fqn;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.Request;
import org.jdiameter.api.ro.ClientRoSession;
import org.jdiameter.client.api.IContainer;
import org.jdiameter.client.api.IMessage;
import org.jdiameter.client.api.parser.IMessageParser;
import org.jdiameter.client.api.parser.ParseException;
import org.jdiameter.client.impl.app.ro.IClientRoSessionData;
import org.jdiameter.common.api.app.ro.ClientRoSessionState;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.diameter.impl.ha.common.AppSessionDataReplicatedImpl;
import org.mobicents.diameter.impl.ha.data.ReplicatedSessionDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:grzegorz.figiel@pro-ids.com"> Grzegorz Figiel (ProIDS sp. z o.o.)</a>
 */
public class ClientRoSessionDataReplicatedImpl extends AppSessionDataReplicatedImpl implements IClientRoSessionData {

  private static final Logger logger = LoggerFactory.getLogger(ClientRoSessionDataReplicatedImpl.class);

  private static final String EVENT_BASED = "EVENT_BASED";
  private static final String REQUEST_TYPE = "REQUEST_TYPE";
  private static final String STATE = "STATE";
  private static final String TXTIMER_ID = "TXTIMER_ID";
  private static final String RETRANSMISSION_TIMER_ID = "RETRANSMISSION_TIMER_ID";
  private static final String TXTIMER_REQUEST = "TXTIMER_REQUEST";
  private static final String BUFFER = "BUFFER";
  private static final String GRA = "GRA";
  private static final String GDDFH = "GDDFH";
  private static final String GCCFH = "GCCFH";
  private static final String GCCSF = "GCCSF";

  private IMessageParser messageParser;

  /**
   * @param nodeFqn
   * @param mobicentsCluster
   * @param container
   */
  public ClientRoSessionDataReplicatedImpl(Fqn<?> nodeFqn, MobicentsCluster mobicentsCluster, IContainer container) {
    super(nodeFqn, mobicentsCluster);

    if (super.create()) {
      setAppSessionIface(this, ClientRoSession.class);
      setClientRoSessionState(ClientRoSessionState.IDLE);
    }

    this.messageParser = container.getAssemblerFacility().getComponentInstance(IMessageParser.class);
  }

  /**
   * @param sessionId
   * @param mobicentsCluster
   * @param container
   */
  public ClientRoSessionDataReplicatedImpl(String sessionId, MobicentsCluster mobicentsCluster, IContainer container) {
    this(Fqn.fromRelativeElements(ReplicatedSessionDatasource.SESSIONS_FQN, sessionId), mobicentsCluster, container);
  }

  public boolean isEventBased() {
    if (exists()) {
      return toPrimitive((Boolean) getNode().get(EVENT_BASED), true);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setEventBased(boolean isEventBased) {
    if (exists()) {
      getNode().put(EVENT_BASED, isEventBased);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public boolean isRequestTypeSet() {
    if (exists()) {
      return toPrimitive((Boolean) getNode().get(REQUEST_TYPE), false);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setRequestTypeSet(boolean requestTypeSet) {
    if (exists()) {
      getNode().put(REQUEST_TYPE, requestTypeSet);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public ClientRoSessionState getClientRoSessionState() {
    if (exists()) {
      return (ClientRoSessionState) getNode().get(STATE);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setClientRoSessionState(ClientRoSessionState state) {
    if (exists()) {
      getNode().put(STATE, state);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public Serializable getTxTimerId() {
    if (exists()) {
      return (Serializable) getNode().get(TXTIMER_ID);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setTxTimerId(Serializable txTimerId) {
    if (exists()) {
      getNode().put(TXTIMER_ID, txTimerId);
    }
    else {
      throw new IllegalStateException();
    }
  }
  
  public Serializable getRetransmissionTimerId() {
    if (exists()) {
      return (Serializable) getNode().get(RETRANSMISSION_TIMER_ID);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setRetransmissionTimerId(Serializable txTimerId) {
    if (exists()) {
      getNode().put(RETRANSMISSION_TIMER_ID, txTimerId);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public Request getTxTimerRequest() {
    if (exists()) {
      byte[] data = (byte[]) getNode().get(TXTIMER_REQUEST);
      if (data != null) {
        try {
          return (Request) this.messageParser.createMessage(ByteBuffer.wrap(data));
        }
        catch (AvpDataException e) {
          logger.error("Unable to recreate Tx Timer Request from buffer.");
          return null;
        }
      }
      else {
        return null;
      }
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setTxTimerRequest(Request txTimerRequest) {
    if (exists()) {
      if (txTimerRequest != null) {
        try {
          byte[] data = this.messageParser.encodeMessage((IMessage) txTimerRequest).array();
          getNode().put(TXTIMER_REQUEST, data);
        }
        catch (ParseException e) {
          logger.error("Unable to encode Tx Timer Request to buffer.");
        }
      }
      else {
        getNode().remove(TXTIMER_REQUEST);
      }
    }
    else {
      throw new IllegalStateException();
    }
  }

  public Request getBuffer() {
    byte[] data = (byte[]) getNode().get(BUFFER);
    if (data != null) {
      try {
        return (Request) this.messageParser.createMessage(ByteBuffer.wrap(data));
      }
      catch (AvpDataException e) {
        logger.error("Unable to recreate message from buffer.");
        return null;
      }
    }
    else {
      return null;
    }
  }

  public void setBuffer(Request buffer) {
    if (buffer != null) {
      try {
        byte[] data = this.messageParser.encodeMessage((IMessage) buffer).array();
        getNode().put(BUFFER, data);
      }
      catch (ParseException e) {
        logger.error("Unable to encode message to buffer.");
      }
    }
    else {
      getNode().remove(BUFFER);
    }
  }

  public int getGatheredRequestedAction() {
    if (exists()) {
      return toPrimitive((Integer) getNode().get(GRA));
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setGatheredRequestedAction(int gatheredRequestedAction) {
    if (exists()) {
      getNode().put(GRA, gatheredRequestedAction);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public int getGatheredCCFH() {
    if (exists()) {
      return toPrimitive((Integer) getNode().get(GCCFH));
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setGatheredCCFH(int gatheredCCFH) {
    if (exists()) {
      getNode().put(GCCFH, gatheredCCFH);
    }
    else {
      throw new IllegalStateException();
    }
  }

  public int getGatheredDDFH() {
    if (exists()) {
      return toPrimitive((Integer) getNode().get(GDDFH));
    }
    else {
      throw new IllegalStateException();
    }
  }

  public void setGatheredDDFH(int gatheredDDFH) {
    if (exists()) {
      getNode().put(GDDFH, gatheredDDFH);
    }
    else {
      throw new IllegalStateException();
    }
  }

  @Override
  public int getGatheredCCSF() {
    if (exists()) {
      return toPrimitive((Integer) getNode().get(GCCSF));
    }
    else {
      throw new IllegalStateException();
    }
  }

  @Override
  public void setGatheredCCSF(int gatheredCCSF) {
    if (exists()) {
      getNode().put(GCCSF, gatheredCCSF);
    }
    else {
      throw new IllegalStateException();
    }
  }

}
