package org.aaronjames.dao;

import org.aaronjames.model.Type;

public interface TypeDao {

    Type getTypeById(int typeId);

    boolean deleteTypeById(int typeId);

    boolean updateType(Type type);

    Type addType(Type type);
}
