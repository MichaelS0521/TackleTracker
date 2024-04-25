import { ICreatedCatch } from 'app/shared/model/created-catch.model';

export interface IFishData {
  id?: number;
  name?: string | null;
  waterType?: string | null;
  weight?: number | null;
  createdCatch?: ICreatedCatch | null;
}

export const defaultValue: Readonly<IFishData> = {};
