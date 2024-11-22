package org.aaronjames.dao;

import org.aaronjames.model.ElementType;

public interface ElementTypeDao {

    ElementType getTypeById(int typeId);

    boolean deleteTypeById(int typeId);

    boolean updateType(ElementType elementType);

    int addType(ElementType elementType);
}
