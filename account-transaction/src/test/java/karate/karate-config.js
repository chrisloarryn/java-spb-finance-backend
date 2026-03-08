function fn() {
  var SimpleDateFormat = Java.type('java.text.SimpleDateFormat');
  var Date = Java.type('java.util.Date');
  var config = {};

  config.baseUrl = karate.properties['karate.baseUrl'] || 'http://127.0.0.1:1204';
  config.clientId = karate.properties['karate.clientId'] || '11111111-1111-1111-1111-111111111111';
  config.today = new SimpleDateFormat('yyyy-MM-dd').format(new Date());
  config.defaultHeaders = {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  };

  config.uuidPattern = '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$';
  config.uuid = '#regex ' + config.uuidPattern;
  config.anyDate = '#? _ != null';
  config.clientIdSchema = '#? _ == "' + config.clientId + '"';

  config.accountCreateResponseSchema = {
    id: config.uuid,
    accountNumber: '#string',
    initialBalance: '#number',
    status: '#boolean',
    accountType: '#string',
    clientId: config.clientIdSchema
  };

  config.accountResponseSchema = {
    id: config.uuid,
    accountNumber: '#string',
    initialBalance: '#number',
    status: '#boolean',
    accountType: '#string',
    clientId: config.clientIdSchema,
    createdAt: config.anyDate
  };

  config.movementResponseSchema = {
    id: config.uuid,
    accountNumber: '#string',
    transactionType: '#string',
    initialBalance: '#number',
    transactionValue: '#number',
    status: '#boolean',
    detail: '#string',
    createdAt: config.anyDate
  };

  config.reportResponseSchema = {
    date: config.anyDate,
    results: '#number',
    data: '#[]'
  };

  config.buildCreateAccountRequest = function(overrides) {
    var uuid = Java.type('java.util.UUID').randomUUID();
    var payload = {
      accountNumber: 'ACC-' + uuid,
      initialBalance: 1000.0,
      status: true,
      accountType: 'SAVINGS',
      clientId: karate.properties['karate.clientId'] || '11111111-1111-1111-1111-111111111111'
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

  config.buildCreateMovementRequest = function(overrides) {
    var payload = {
      transactionType: 'SAVINGS',
      accountNumber: 'ACC-NOT-SET',
      transactionValue: 100.0,
      status: true
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
