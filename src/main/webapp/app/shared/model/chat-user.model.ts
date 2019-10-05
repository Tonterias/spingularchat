import { Moment } from 'moment';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { IChatInvitation } from 'app/shared/model/chat-invitation.model';
import { IChatMessage } from 'app/shared/model/chat-message.model';
import { IChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';
import { IChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';
import { IChatNotification } from 'app/shared/model/chat-notification.model';

export interface IChatUser {
  id?: number;
  creationDate?: Moment;
  bannedUser?: boolean;
  userId?: number;
  chatPhotoId?: number;
  chatRooms?: IChatRoom[];
  senders?: IChatInvitation[];
  receivers?: IChatInvitation[];
  chatMessages?: IChatMessage[];
  chatRoomAllowedUsers?: IChatRoomAllowedUser[];
  chatOffensiveMessages?: IChatOffensiveMessage[];
  chatNotifications?: IChatNotification[];
}

export class ChatUser implements IChatUser {
  constructor(
    public id?: number,
    public creationDate?: Moment,
    public bannedUser?: boolean,
    public userId?: number,
    public chatPhotoId?: number,
    public chatRooms?: IChatRoom[],
    public senders?: IChatInvitation[],
    public receivers?: IChatInvitation[],
    public chatMessages?: IChatMessage[],
    public chatRoomAllowedUsers?: IChatRoomAllowedUser[],
    public chatOffensiveMessages?: IChatOffensiveMessage[],
    public chatNotifications?: IChatNotification[]
  ) {
    this.bannedUser = this.bannedUser || false;
  }
}
