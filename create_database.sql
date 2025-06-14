CREATE TABLE USERS (
   ID STRING PRIMARY KEY NOT NULL,
   USERNAME STRING NOT NULL,
   PASSWORD STRING NOT NULL
);

CREATE TABLE FILES (
   UPLOADER_ID STRING NOT NULL,
   FILENAME STRING PRIMARY KEY NOT NULL,
   CONTENT_TYPE STRING NOT NULL,
   PERMISSION_TYPE STRING NOT NULL,
   SIZE INT NOT NULL,
   PATH STRING NOT NULL,
   DATE INTEGER NOT NULL
);

INSERT INTO USERS (id, username, password) VALUES
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'user1', '65000:WtMelsCyM6WLRXD1i6vCNGBwVSf3vq6TpJ/M/0yH/x4=:fUFeHzh8OfEucuwS3xZGkqebzW//TVPzd83vZWtZ0ncYsUqpC5aSRYXbeQ30AUABx+hcCylcvPxAv6RqdrRnrA==:512'),
    ('38863bb0-da0d-4c67-8004-ee82f350ecb8', 'user2', '65000:pk5hn0oO4G4eUWb97uu+ukIrEX5yznJ3Opa1HtUyQeY=:nEAMTYBxVUXK3beyWwv8kbR7w+IwLZP8S5JlZ/8VIDChcvB2guOuq/G1OKxqTIdi7YXxbsrnydy9ftyhyrCjSw==:512'),
    ('3d159655-eb7c-4982-b72a-af5eff9cfab3', 'user3', '65000:44BTL/uMQBR3lOBftF0ufPED2Z6u4R4sf0Exm2B3ctQ=:n1VBtuLcdFXo9Ams0QPSZWRD8tIoN7/CcO8vJ/k7kGcPa7f/NSJrYYaoKmrogAC/NAjeZdEY6+QJR4e/RML3Lg==:512');

INSERT INTO FILES (uploader_id, filename, content_type, permission_type, size, path, date) VALUES
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file1', 'text', 'PUBLIC', 1, 'saved_files/file1.zip', 1749659227),
    ('38863bb0-da0d-4c67-8004-ee82f350ecb8', 'file2', 'text', 'PUBLIC', 1, 'saved_files/file2.zip', 1749659227),
    ('3d159655-eb7c-4982-b72a-af5eff9cfab3', 'file3', 'text', 'PUBLIC', 1, 'saved_files/file3.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file4', 'text', 'PROTECTED', 1, 'saved_files/file4.zip', 1749659227),
    ('38863bb0-da0d-4c67-8004-ee82f350ecb8', 'file5', 'text', 'PROTECTED', 1, 'saved_files/file5.zip', 1749659227),
    ('3d159655-eb7c-4982-b72a-af5eff9cfab3', 'file6', 'text', 'PROTECTED', 1, 'saved_files/file6.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file7', 'text', 'PRIVATE', 1, 'saved_files/file7.zip', 1749659227),
    ('38863bb0-da0d-4c67-8004-ee82f350ecb8', 'file8', 'text', 'PRIVATE', 1, 'saved_files/file8.zip', 1749659227),
    ('3d159655-eb7c-4982-b72a-af5eff9cfab3', 'file9', 'text', 'PRIVATE', 1, 'saved_files/file9.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file10', 'text', 'PUBLIC', 1, 'saved_files/file10.zip', 1749659227),
    ('38863bb0-da0d-4c67-8004-ee82f350ecb8', 'file11', 'text', 'PUBLIC', 1, 'saved_files/file11.zip', 1749659227),
    ('3d159655-eb7c-4982-b72a-af5eff9cfab3', 'file12', 'text', 'PUBLIC', 1, 'saved_files/file12.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file13', 'text', 'PROTECTED', 1, 'saved_files/file13.zip', 1749659227),
    ('38863bb0-da0d-4c67-8004-ee82f350ecb8', 'file14', 'text', 'PROTECTED', 1, 'saved_files/file14.zip', 1749659227),
    ('3d159655-eb7c-4982-b72a-af5eff9cfab3', 'file15', 'text', 'PROTECTED', 1, 'saved_files/file15.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file16', 'text', 'PUBLIC', 1, 'saved_files/file16.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file17', 'text', 'PUBLIC', 1, 'saved_files/file17.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file18', 'text', 'PUBLIC', 1, 'saved_files/file18.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file19', 'text', 'PUBLIC', 1, 'saved_files/file19.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file20', 'text', 'PUBLIC', 1, 'saved_files/file20.zip', 1749659227),
    ('ca1d47fd-b553-4dab-bc31-3cd89cd42fa5', 'file21', 'text', 'PUBLIC', 1, 'saved_files/file21.zip', 1749659227);
