MATCH (m: POST | COMMENT {id: $messageId }) RETURN m.creationDate as messageCreationDate, m.content AS messageContent, m.imageFile as messageImageFile