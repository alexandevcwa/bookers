create table Paises
(
    ps_id     serial primary key,
    ps_nombre varchar(75) unique not null
);

create table Departamentos
(
    depto_id     serial primary key,
    depto_nombre varchar(75) unique                not null,
    ps_id        integer references Paises (ps_id) not null
);

create table Municipios
(
    muni_id     serial primary key,
    muni_nombre varchar(75) unique                          not null,
    depto_id    integer references Departamentos (depto_id) not null
);

create table Usuarios
(
    usr_id        serial primary key,
    usr_nombres   varchar(100)                            not null,
    usr_apellidos varchar(100)                            not null,
    usr_direccion varchar(150)                            not null,
    usr_cui       varchar(13) unique                      not null,
    usr_telefono  varchar(9)                              not null,
    usr_email     varchar(45) unique                      not null,
    usr_password  varchar(75)                             not null,
    usr_registro  timestamp                               null default current_timestamp,
    muni_id       integer references Municipios (muni_id) not null

);


create type estado_prestamo as enum ('PRESTAMO_RESERVA','PRESTAMO_ACTIVO','PRESTAMO_COMPLETO','PRESTAMO_INCOMPLETO');

create table Prestamos
(
    pre_id        serial primary key,
    pres_fecha    date default current_date null,
    pres_estado   estado_prestamo           not null,
    pres_fecvence date                      not null,
    pres_fecdevo  date                      not null,
    usr_id        integer references Usuarios (usr_id)
);

create table Librerias
(
    lib_id        serial primary key,
    lib_nombre    varchar(75)  not null,
    lib_direccion varchar(100) not null,
    lib_zipcode   varchar(5)   not null,
    lib_email     varchar(45)  null,
    lib_telefono  varchar(9)   null
);

create table Editoriales
(
    edit_id     serial primary key,
    edit_nombre varchar(50) unique not null
);

create table Autores
(
    aut_id     serial primary key,
    aut_nombre varchar(50) unique not null
);

create table Categorias
(
    catego_id     serial primary key,
    catego_nombre varchar(50) unique not null

);

create table Libros
(
    lbr_sku     serial primary key,
    lbr_titulo  varchar(50)                               not null,
    lbr_publica date                                      not null,
    lbr_cover   bytea                                     null,
    lbr_previa  text                                      not null,
    lbr_isbn    varchar(25) unique                        not null,
    aut_id      integer references Autores (aut_id)       not null,
    edit_id     integer references Editoriales (edit_id)  not null,
    catego_id   integer references Categorias (catego_id) not null
);

create table LibrosDatos
(
    lbd_id       serial primary key,
    lbd_archivo  bytea                                      not null,
    lbd_narchivo varchar(50)                                not null,
    lbd_ntipo    text                                       not null,
    lbr_sku      integer references Libros (lbr_sku) unique not null
);



create table LibreriasLibros
(
    lib_id  integer references Librerias (lib_id),
    lbr_sku integer references Libros (lbr_sku),
    primary key (lib_id, lbr_sku)
);

create table PrestamosDetalle
(
    presd_id serial primary key,
    pres_id  integer not null references Prestamos (pre_id),
    lib_id   integer not null,
    lbr_sku  integer not null,
    foreign key (lib_id, lbr_sku) references LibreriasLibros (lib_id, lbr_sku)
);

create table Politicas
(
    plt_id      serial primary key,
    plt_titulo  varchar(100) unique not null,
    plt_descrip text                not null,
    plt_monto   money default 0     null
);

create table PrestamosDetallePoliticas
(
    presd_id integer references PrestamosDetalle (presd_id),
    plt_id   integer references Politicas (plt_id),
    primary key (presd_id, plt_id)
);


create table Inventarios
(
    lib_id         integer                                            not null,
    lbr_sku        integer                                            not null,
    inv_disponible integer                                            null,
    inv_stockmin   integer                                            null,
    inv_stockmax   integer                                            null,
    inv_actualiza  timestamp default current_timestamp                null,
    primary key (lib_id, lbr_sku),
    foreign key (lib_id, lbr_sku) references LibreriasLibros (lib_id, lbr_sku)
);

create type tipo_movimiento_inventario as enum ('ENTRADA_DEVOLUCION',
    'ENTRADA_NUEVO',
    'SALIDA_PRESTAMO',
    'SALIDA_DESGASTE',
    'SALIDA_PERDIDA',
    'AJUSTE_INV_ENTRADA',
    'AJUSTE_INV_SALIDA',
    'SALIDA_TRASLADO',
    'ENTRADA_TRASLADO'
    );

create table MovimientosInventarios
(
    mvn_id         serial primary key,
    mvn_tipo       tipo_movimiento_inventario                         not null,
    mvn_cantidad   integer                                            not null,
    mvn_disponible integer                                            not null,
    mvn_fecha      timestamp default current_timestamp                null,
    mvn_total      integer                                            not null,
    mvn_referencia integer references MovimientosInventarios (mvn_id) null,
    lib_id         integer                                            not null,
    lbr_sku        integer                                            not null,
    foreign key ( lib_id, lbr_sku)
        references Inventarios (lib_id, lbr_sku)
);

create table Roles
(
    role_id     serial primary key,
    role_nombre varchar(20) unique not null
);

create table UsuariosRoles
(
    usr_id  integer not null references Usuarios (usr_id),
    role_id integer not null references Roles (role_id),
    primary key (usr_id, role_id)
);

create table uris
(
    uri_id     serial primary key,
    uri_patter varchar(100) unique not null
);

create table urisroles
(
    uri_id  integer references uris (uri_id),
    role_id integer references roles (role_id),
    primary key (uri_id, role_id)
);


create index idx_usuarios_cui on usuarios using hash (usr_cui);
create index idx_usuarios_email on usuarios using hash (usr_email);
create index idx_librerias on librerias using hash (lib_email);
create index idx_libros_titulo on libros using spgist (lbr_titulo);
create index idx_libros_isbn on libros using hash (lbr_isbn);
create index idx_libros_publica on libros using hash (lbr_publica);