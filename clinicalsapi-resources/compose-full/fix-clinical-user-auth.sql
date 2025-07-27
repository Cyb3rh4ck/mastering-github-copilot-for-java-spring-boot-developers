-- Eliminar el usuario si ya existe con host incorrecto
DROP USER IF EXISTS 'clinical_user'@'%';

-- Crear el usuario compatible con herramientas externas
CREATE USER 'clinical_user'@'%' IDENTIFIED WITH mysql_native_password BY 'Cl1n1c@ls!2025';

-- Otorgar privilegios sobre la base de datos clinicals
GRANT ALL PRIVILEGES ON clinicals.* TO 'clinical_user'@'%';

-- Aplicar cambios
FLUSH PRIVILEGES;