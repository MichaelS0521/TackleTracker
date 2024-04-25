export interface IUserProfile {
  id?: number;
  email?: string | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
