package com.maspim

class UsuarioRole implements Serializable {
    Usuario usuario
    Role role

    static mapping = {
        id composite: ['usuario', 'role']
    }

    static constraints = {
        usuario nullable: false
        role nullable: false
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof UsuarioRole)) return false

        UsuarioRole that = (UsuarioRole) o

        if (usuario != that.usuario) return false
        if (role != that.role) return false

        return true
    }

    int hashCode() {
        int result
        result = (usuario != null ? usuario.hashCode() : 0)
        result = 31 * result + (role != null ? role.hashCode() : 0)
        return result
    }
}
