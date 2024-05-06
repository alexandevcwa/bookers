-- Roles del sistema
insert into Roles(role_nombre)
values ('ADMINISTRADOR');
insert into Roles(role_nombre)
values ('CLIENTE');
insert into Roles(role_nombre)
values ('PUBLICO');

insert into uris (uri_patter)
values ('/api/v1/auth/**'),
       ('/swagger-ui/**'),
       ('/v3/api-docs/**');

insert into urisroles
values (1, 3),
       (2, 3),
       (3, 3);

insert into paises(ps_nombre)
values ('GUATEMALA');
insert into departamentos (depto_nombre, ps_id)
values ('GUATEMALA C.C.', 1);

insert into municipios (muni_nombre, depto_id)
values ('SAN RAYMUNDO', 1),
       ('ZONA 1', 1),
       ('ZONA 2', 1),
       ('ZONA 3', 1);

insert into uris (uri_patter)
values ('/api/v1/locaciones/**');

insert into urisroles
values ((select uri_id from uris where uri_patter = '/api/v1/locaciones/**'), 3);

insert into Librerias (lib_nombre, lib_direccion, lib_zipcode, lib_email, lib_telefono)
values ('LIBRERIA DIGITAL', 'ONLINE', '0000', 'dgbook@boookers.com', '12345678');

insert into periodoscontables (per_anio, per_periodo, per_inicial, per_final)
values (2024, 424, '2024-04-01', '2024-04-30');
insert into periodoscontables (per_anio, per_periodo, per_inicial, per_final)
values (2024, 524, '2024-05-01', '2024-05-31');
insert into periodoscontables (per_anio, per_periodo, per_inicial, per_final)
values (2024, 624, '2024-06-01', '2024-06-30');

insert into categorias (catego_nombre)
values ('OTRO');
insert into autores (aut_nombre)
values ('OTRO');
insert into editoriales (edit_nombre)
values ('OTRO');