package com.junorz.travelbook.context.orm;

import javax.persistence.EntityManager;

public interface Repository {
    
    public EntityManager em();
    
}
