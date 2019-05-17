package com.junorz.travelbook.context.orm;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DefaultRepository implements Repository {
    
    @PersistenceContext(unitName = "travelbook")
    private EntityManager em;
    
    public EntityManager em() {
        return em;
    }
    
}
