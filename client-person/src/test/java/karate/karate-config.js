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
    name: '#string',
    clientId: '#string',
    password: '#string',
    gender: '#string',
    age: '#number',
    identifier: '#string',
    address: '#string',
    phoneNumber: '#string',
    status: '#string'
  };

  config.buildCreateClientRequest = function(overrides) {
    var uuid = Java.type('java.util.UUID').randomUUID();
    var payload = {
      name: 'Karate Client ' + uuid,
      clientId: 'client-' + uuid,
      password: 'Secret123',
      gender: 'M',
      age: 31,
      identifier: 'karate-' + uuid + '@example.com',
      address: 'Main Street 123',
      phoneNumber: '+56911111111',
      status: 'ACTIVE'
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
      name: 'Updated Client ' + uuid,
      clientId: 'updated-client-' + uuid,
      password: 'UpdatedSecret123',
      gender: 'F',
      age: 33,
      identifier: 'updated-' + uuid + '@example.com',
      address: 'Updated Street 456',
      phoneNumber: '+56922222222',
      status: 'ACTIVE'
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
