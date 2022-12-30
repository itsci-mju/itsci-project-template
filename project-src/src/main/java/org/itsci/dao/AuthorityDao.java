package org.itsci.attendance.dao;

import org.itsci.attendance.model.Authority;

public interface AuthorityDao {
    void delete(int id);
    Authority findByAuthority(String authority);
}
