# Namma Mela Backend

## Tech Stack
- Node.js
- Express
- MongoDB with Mongoose
- JWT authentication

## Setup
1. Copy `.env.example` to `.env`
2. Update `MONGODB_URI` and `JWT_SECRET`
3. Install dependencies with `npm install`
4. Seed initial data with `npm run seed`
5. Start the API with `npm run dev`

## API Endpoints
- `POST /api/register`
- `POST /api/login`
- `POST /api/admin/login`
- `GET /api/play`
- `POST /api/play`
- `GET /api/seats`
- `POST /api/booking`
- `GET /api/comments`
- `POST /api/comments`
- `GET /api/cast`
- `POST /api/cast`
- `DELETE /api/reset-seats`

## Authentication
- Send `Authorization: Bearer <token>` for all protected endpoints.
- Admin-only endpoints require an admin token.
