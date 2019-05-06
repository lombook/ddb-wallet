package com.jinglitong.wallet.api.model;

import javax.persistence.*;

@Table(name = "role_menu")
public class RoleMenu {
    @Id
    @Column(name = "menu_id")
    private String menuId;

    @Id
    @Column(name = "role_id")
    private String roleId;

    /**
     * @return menu_id
     */
    public String getMenuId() {
        return menuId;
    }

    /**
     * @param menuId
     */
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    /**
     * @return role_id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}