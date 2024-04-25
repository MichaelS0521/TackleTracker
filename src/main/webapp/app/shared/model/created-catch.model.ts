import dayjs from 'dayjs';
import { IBaitData } from 'app/shared/model/bait-data.model';
import { IWeatherData } from 'app/shared/model/weather-data.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ICreatedCatch {
  id?: number;
  datestamp?: dayjs.Dayjs | null;
  location?: string | null;
  baitdata?: IBaitData[] | null;
  weather?: IWeatherData | null;
  user?: IUserProfile | null;
}

export const defaultValue: Readonly<ICreatedCatch> = {};
