# Namma Mela Database Schema

## Remote Database: MongoDB

### `users`
- `_id`: ObjectId
- `name`: String
- `email`: String
- `password`: String
- `role`: `user` or `admin`
- `createdAt`: Date
- `updatedAt`: Date

### `play`
- `_id`: ObjectId
- `title`: String
- `genre`: String
- `duration`: String
- `description`: String
- `poster`: String
- `createdAt`: Date
- `updatedAt`: Date

### `bookings`
- `_id`: ObjectId
- `user`: ObjectId reference to `users`
- `customerName`: String
- `seats`: Array of seat codes like `S1`, `S2`
- `timestamp`: Date
- `createdAt`: Date
- `updatedAt`: Date

### `comments`
- `_id`: ObjectId
- `name`: String
- `message`: String
- `time`: Date
- `createdAt`: Date
- `updatedAt`: Date

### `cast`
- `_id`: ObjectId
- `name`: String
- `role`: String
- `bio`: String
- `image`: String
- `createdAt`: Date
- `updatedAt`: Date

## Local Database: Room

### `users`
- `id`: String
- `name`: String
- `email`: String
- `role`: String

### `play`
- `id`: String
- `title`: String
- `genre`: String
- `duration`: String
- `description`: String
- `poster`: String

### `bookings`
- `id`: String
- `userId`: String
- `customerName`: String
- `seatsCsv`: String
- `timestamp`: String

### `comments`
- `id`: String
- `name`: String
- `message`: String
- `time`: String

### `cast`
- `id`: String
- `name`: String
- `role`: String
- `bio`: String
- `image`: String
