package org.alexandevcwa.application.model.repository;

import org.alexandevcwa.application.model.Usuario;

public interface UsuarioRepositoryImp<T> extends CRUDRepository<T> {

    /**
     * Búsqueda por correo electrónico
     *
     * @param email Correo electrónico relacionado con el usuario a buscar
     * @return Objeto {@link Usuario} con los datos del usuario
     */
    T findByEmail(String email);

    /**
     * Busqueda de Usuario por medio de CUI y Password
     *
     * @param cui      Número de CUI del usuario
     * @param password Contraseña del usuario
     * @return Objeto de tipo {@link Usuario}
     */
    T findByCuiAndPassword(String cui, String password);

    /**
     * Verifica la existencia de un usuario por medio de la autenticación de sus credenciales
     *
     * @param cui      Núnero de CUI del usuario
     * @param password Contraseña del usuario
     * @return {@link Boolean} True: significa que el número de CUI y Contraseña son correctas, False: significa que
     * las credenciales son incorrectas
     */
    boolean existsByCuiAndPassword(String cui, String password);


    /**
     * Verificación de CUI existent
     *
     * @param cui Número de CUI para verificar
     * @return {@link Boolean} True: existe, False: no existe
     */
    boolean existsByCui(String cui);

    /**
     * Verificación de email existente
     *
     * @param email Email a verificar
     * @return {@link Boolean} True: existen, False: no existe
     */
    boolean existsByEmail(String email);

    /**
     * Verificación de teléfono existente
     *
     * @param telefono Teléfono a verificar
     * @return {@link Boolean} True: existe, False: no existe
     */
    boolean existsByTelefono(String telefono);

    /**
     * Método para verificación de permisos de un usuario al acceder a un apartado administrativo
     *
     * @param usrId ID del usuario a verificar los permisos
     * @return {@link Boolean} True: el usuario tiene permisos, False: el usuario no tiene permisos
     */
    boolean hasGrants(int usrId);
}
