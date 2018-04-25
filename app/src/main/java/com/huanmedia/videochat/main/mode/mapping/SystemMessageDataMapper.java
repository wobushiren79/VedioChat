/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
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
package com.huanmedia.videochat.main.mode.mapping;

import com.huanmedia.videochat.main.mode.SystemMessage;
import com.huanmedia.videochat.repository.entity.SMsgEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
public class SystemMessageDataMapper {

  public SystemMessageDataMapper() {}

  public SystemMessage transform(SMsgEntity sMsgEntity) {
    if (sMsgEntity == null) {
      throw new IllegalArgumentException("Cannot transform a null value");
    }
    final SystemMessage systemMode = new SystemMessage();
    systemMode.setIsRed(sMsgEntity.getIsRed());
    systemMode.setCreatetime(sMsgEntity.getCreatetime());
    systemMode.setStrtime(sMsgEntity.getStrtime());
    systemMode.setDesc(sMsgEntity.getDesc());
    systemMode.setMsgId(sMsgEntity.getId());
    systemMode.setTitle(sMsgEntity.getTitle());
    systemMode.setType(sMsgEntity.getType());
    systemMode.setUrl(sMsgEntity.getUrl());
    systemMode.setIsRed(sMsgEntity.getIsRed());
    return systemMode;
  }

  public Collection<SystemMessage> transform(Collection<SMsgEntity> usersCollection) {
    Collection<SystemMessage> userModelsCollection;

    if (usersCollection != null && !usersCollection.isEmpty()) {
      userModelsCollection = new ArrayList<>();
      for (SMsgEntity user : usersCollection) {
        userModelsCollection.add(transform(user));
      }
    } else {
      userModelsCollection = Collections.emptyList();
    }

    return userModelsCollection;
  }
}
