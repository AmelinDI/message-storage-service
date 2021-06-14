# Сервис message-storage-service

Данный сервис представляет хранилище сообщений

Ответственный за сервис: Дмитрий Демидов

## План работ:

#### Создать таблицу message. Написать liquibase скрипт. Данная таблица хранит сообщения пользователей

Поля таблицы message

  * id: string
  * sender: string
  * recipient: string
  * content: string
  * message_timestamp: LocalDateTime
  * last_access_time: LocalDateTime (время сохранения в БД)

### Создать соответствующий класс Message.

### Реализовать rest-контроллер /storage/**/*, который обеспечивает работу с сообщениями пользователей

План работы:


Дмитрий Демидов
 - Написать ликубейс скрипты и разместить из в папке sql, написать структуру Message, MessageEntity, getters & setters, toString
 - Save all messages PUT /storage/message/all
 - Delete message by id DELETE /storage/message?messageId={messageId}

Амелин Дмитрий
 - Get all messages between sender and receiver since timestamp GET /storage/message/allSinceTime?sender={sender}&receiver={receiver}&timestamp={timestamp}
 - Save message PUT /storage/message

Полинов Тимур
 - Get message by id GET /storage/message?messageId={messageId}
 - Get all messages between sender and receiver GET /storage/message/all?sender={sender}&receiver={receiver}

Критерии приемки:
- методы сервиса должны быть протестированы.