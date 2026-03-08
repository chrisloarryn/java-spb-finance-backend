function fn() {
  var config = {};

  config.baseUrl = karate.properties['karate.baseUrl'] || 'http://127.0.0.1:1203';
  config.defaultHeaders = {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  };

  config.uuidPattern = '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$';
  config.uuid = '#regex ' + config.uuidPattern;
  config.anyDate = '#? _ != null';

  config.clientResponseSchema = {
    id: config.uuid,
    nombre: '#string',
    clienteid: '#string',
    contrasena: '#string',
    genero: '#string',
    edad: '#number',
    identificacion: '#string',
    direccion: '#string',
    telefono: '#string',
    estado: '#string'
  };

  config.buildCreateClientRequest = function(overrides) {
    var uuid = Java.type('java.util.UUID').randomUUID();
    var payload = {
      nombre: 'Karate Client ' + uuid,
      clienteid: 'client-' + uuid,
      contrasena: 'Secret123',
      genero: 'M',
      edad: 31,
      identificacion: 'karate-' + uuid + '@example.com',
      direccion: 'Main Street 123',
      telefono: '+56911111111',
      estado: 'ACTIVO'
    };
    if (overrides) {
      for (var key in overrides) {
        if (Object.prototype.hasOwnProperty.call(overrides, key) && overrides[key] !== null && overrides[key] !== undefined) {
          payload[key] = overrides[key];
        }
      }
    }
    return payload;
  };

  config.buildUpdateClientRequest = function(overrides) {
    var uuid = Java.type('java.util.UUID').randomUUID();
    var payload = {
      nombre: 'Updated Client ' + uuid,
      clienteid: 'updated-client-' + uuid,
      contrasena: 'UpdatedSecret123',
      genero: 'F',
      edad: 33,
      identificacion: 'updated-' + uuid + '@example.com',
      direccion: 'Updated Street 456',
      telefono: '+56922222222',
      estado: 'ACTIVO'
    };
    if (overrides) {
      for (var key in overrides) {
        if (Object.prototype.hasOwnProperty.call(overrides, key) && overrides[key] !== null && overrides[key] !== undefined) {
          payload[key] = overrides[key];
        }
      }
    }
    return payload;
  };

  return config;
}
