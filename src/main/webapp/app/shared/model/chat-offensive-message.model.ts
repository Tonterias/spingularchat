import { Moment } from 'moment';

export interface IChatOffensiveMessage {
  id?: number;
  creationDate?: Moment;
  isOffensive?: boolean;
  chatUserId?: number;
  chatMessageId?: number;
}

export class ChatOffensiveMessage implements IChatOffensiveMessage {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public isOffensive?: boolean,
    public chatUserId?: number,
    public chatMessageId?: number
  ) {
    this.isOffensive = this.isOffensive || false;
  }
}
