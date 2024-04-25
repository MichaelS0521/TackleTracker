import { ICreatedCatch } from 'app/shared/model/created-catch.model';

export interface IBaitData {
  id?: number;
  type?: string | null;
  color?: string | null;
  hard?: boolean | null;
  soft?: boolean | null;
  catches?: ICreatedCatch[] | null;
}

export const defaultValue: Readonly<IBaitData> = {
  hard: false,
  soft: false,
};
