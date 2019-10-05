import { IChatNotification } from 'app/shared/model/chat-notification.model';
import { IChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';

export interface IChatMessage {
  id?: number;
  messageSentAt?: string;
  message?: string;
  isReceived?: boolean;
  isDelivered?: boolean;
  chatNotifications?: IChatNotification[];
  chatOffensiveMessages?: IChatOffensiveMessage[];
  chatRoomId?: number;
  chatUserId?: number;
}

export class ChatMessage implements IChatMessage {
  constructor(
    public id?: number,
    public messageSentAt?: string,
    public message?: string,
    public isReceived?: boolean,
    public isDelivered?: boolean,
    public chatNotifications?: IChatNotification[],
    public chatOffensiveMessages?: IChatOffensiveMessage[],
    public chatRoomId?: number,
    public chatUserId?: number
  ) {
    this.isReceived = this.isReceived || false;
    this.isDelivered = this.isDelivered || false;
  }
}
