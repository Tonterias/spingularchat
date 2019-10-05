import { Moment } from 'moment';

export interface IChatRoomAllowedUser {
  id?: number;
  creationDate?: Moment;
  bannedUser?: boolean;
  bannedDate?: Moment;
  chatRoomId?: number;
  chatUserId?: number;
}

export class ChatRoomAllowedUser implements IChatRoomAllowedUser {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public bannedUser?: boolean,
    public bannedDate?: Moment,
    public chatRoomId?: number,
    public chatUserId?: number
  ) {
    this.bannedUser = this.bannedUser || false;
  }
}
