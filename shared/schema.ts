import { pgTable, serial, varchar, text, timestamp, integer, boolean, jsonb, foreignKey, primaryKey } from 'drizzle-orm/pg-core';
import { createInsertSchema } from 'drizzle-zod';
import { z } from 'zod';

// Core entities
export const devotees = pgTable('devotees', {
  id: serial('id').primaryKey(),
  name: varchar('name', { length: 255 }).notNull(),
  email: varchar('email', { length: 255 }),
  phone: varchar('phone', { length: 20 }),
  statusId: integer('status_id'),
  courses: jsonb('courses'),
  notes: text('notes'),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export const namhattas = pgTable('namhattas', {
  id: serial('id').primaryKey(),
  name: varchar('name', { length: 255 }).notNull(),
  secretary: varchar('secretary', { length: 255 }).notNull(),
  status: varchar('status', { length: 50 }).notNull().default('active'),
  shraddhakutirId: integer('shraddhakutir_id'),
  establishedDate: timestamp('established_date'),
  notes: text('notes'),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export const devotionalStatuses = pgTable('devotional_statuses', {
  id: serial('id').primaryKey(),
  name: varchar('name', { length: 100 }).notNull(),
  description: text('description'),
  hierarchy: integer('hierarchy').notNull().default(0),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export const shraddhakutirs = pgTable('shraddhakutirs', {
  id: serial('id').primaryKey(),
  name: varchar('name', { length: 255 }).notNull(),
  description: text('description'),
  status: varchar('status', { length: 50 }).notNull().default('active'),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export const leaders = pgTable('leaders', {
  id: serial('id').primaryKey(),
  name: varchar('name', { length: 255 }).notNull(),
  title: varchar('title', { length: 255 }).notNull(),
  email: varchar('email', { length: 255 }),
  phone: varchar('phone', { length: 20 }),
  hierarchy: integer('hierarchy').notNull().default(0),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

// Address entities
export const addresses = pgTable('addresses', {
  id: serial('id').primaryKey(),
  country: varchar('country', { length: 100 }),
  state: varchar('state', { length: 100 }),
  district: varchar('district', { length: 100 }),
  subDistrict: varchar('sub_district', { length: 100 }),
  village: varchar('village', { length: 100 }),
  postalCode: varchar('postal_code', { length: 20 }),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export const devoteeAddresses = pgTable('devotee_addresses', {
  id: serial('id').primaryKey(),
  devoteeId: integer('devotee_id').notNull().references(() => devotees.id, { onDelete: 'cascade' }),
  addressId: integer('address_id').notNull().references(() => addresses.id, { onDelete: 'cascade' }),
  landmark: text('landmark'),
  addressType: varchar('address_type', { length: 50 }).notNull().default('primary'),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export const namhattaAddresses = pgTable('namhatta_addresses', {
  id: serial('id').primaryKey(),
  namhattaId: integer('namhatta_id').notNull().references(() => namhattas.id, { onDelete: 'cascade' }),
  addressId: integer('address_id').notNull().references(() => addresses.id, { onDelete: 'cascade' }),
  landmark: text('landmark'),
  addressType: varchar('address_type', { length: 50 }).notNull().default('primary'),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

// Authentication and user management
export const users = pgTable('users', {
  id: serial('id').primaryKey(),
  username: varchar('username', { length: 255 }).notNull().unique(),
  passwordHash: varchar('password_hash', { length: 255 }).notNull(),
  role: varchar('role', { length: 50 }).notNull(),
  active: boolean('active').notNull().default(true),
  lastLogin: timestamp('last_login'),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export const userDistricts = pgTable('user_districts', {
  id: serial('id').primaryKey(),
  userId: integer('user_id').notNull().references(() => users.id, { onDelete: 'cascade' }),
  districtCode: varchar('district_code', { length: 10 }).notNull(),
  districtName: varchar('district_name', { length: 255 }).notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export const userSessions = pgTable('user_sessions', {
  id: serial('id').primaryKey(),
  userId: integer('user_id').notNull().references(() => users.id, { onDelete: 'cascade' }),
  sessionToken: varchar('session_token', { length: 255 }).notNull().unique(),
  expiresAt: timestamp('expires_at').notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export const jwtBlacklist = pgTable('jwt_blacklist', {
  id: serial('id').primaryKey(),
  token: varchar('token', { length: 500 }).notNull().unique(),
  expiresAt: timestamp('expires_at').notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

// Insert schemas
export const insertDevoteeSchema = createInsertSchema(devotees).omit({
  id: true,
  createdAt: true,
  updatedAt: true,
});

export const insertNamhattaSchema = createInsertSchema(namhattas).omit({
  id: true,
  createdAt: true,
  updatedAt: true,
});

export const insertDevotionalStatusSchema = createInsertSchema(devotionalStatuses).omit({
  id: true,
  createdAt: true,
  updatedAt: true,
});

export const insertShraddhakutirSchema = createInsertSchema(shraddhakutirs).omit({
  id: true,
  createdAt: true,
  updatedAt: true,
});

export const insertLeaderSchema = createInsertSchema(leaders).omit({
  id: true,
  createdAt: true,
  updatedAt: true,
});

export const insertAddressSchema = createInsertSchema(addresses).omit({
  id: true,
  createdAt: true,
  updatedAt: true,
});

export const insertUserSchema = createInsertSchema(users).omit({
  id: true,
  createdAt: true,
  updatedAt: true,
  lastLogin: true,
});

// Types
export type Devotee = typeof devotees.$inferSelect;
export type InsertDevotee = z.infer<typeof insertDevoteeSchema>;

export type Namhatta = typeof namhattas.$inferSelect;
export type InsertNamhatta = z.infer<typeof insertNamhattaSchema>;

export type DevotionalStatus = typeof devotionalStatuses.$inferSelect;
export type InsertDevotionalStatus = z.infer<typeof insertDevotionalStatusSchema>;

export type Shraddhakutir = typeof shraddhakutirs.$inferSelect;
export type InsertShraddhakutir = z.infer<typeof insertShraddhakutirSchema>;

export type Leader = typeof leaders.$inferSelect;
export type InsertLeader = z.infer<typeof insertLeaderSchema>;

export type Address = typeof addresses.$inferSelect;
export type InsertAddress = z.infer<typeof insertAddressSchema>;

export type User = typeof users.$inferSelect;
export type InsertUser = z.infer<typeof insertUserSchema>;

export type DevoteeAddress = typeof devoteeAddresses.$inferSelect;
export type NamhattaAddress = typeof namhattaAddresses.$inferSelect;
export type UserDistrict = typeof userDistricts.$inferSelect;
export type UserSession = typeof userSessions.$inferSelect;
export type JwtBlacklistEntry = typeof jwtBlacklist.$inferSelect;

// Additional validation schemas for complex operations
export const loginSchema = z.object({
  username: z.string().min(1, 'Username is required'),
  password: z.string().min(1, 'Password is required'),
});

export const devoteeWithAddressSchema = insertDevoteeSchema.extend({
  country: z.string().optional(),
  state: z.string().optional(),
  district: z.string().optional(),
  subDistrict: z.string().optional(),
  village: z.string().optional(),
  postalCode: z.string().optional(),
  landmark: z.string().optional(),
});

export const namhattaWithAddressSchema = insertNamhattaSchema.extend({
  country: z.string().optional(),
  state: z.string().optional(),
  district: z.string().optional(),
  subDistrict: z.string().optional(),
  village: z.string().optional(),
  postalCode: z.string().optional(),
  landmark: z.string().optional(),
});

export type LoginRequest = z.infer<typeof loginSchema>;
export type DevoteeWithAddress = z.infer<typeof devoteeWithAddressSchema>;
export type NamhattaWithAddress = z.infer<typeof namhattaWithAddressSchema>;