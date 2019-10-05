import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChatOffensiveMessage } from 'app/shared/model/chat-offensive-message.model';

type EntityResponseType = HttpResponse<IChatOffensiveMessage>;
type EntityArrayResponseType = HttpResponse<IChatOffensiveMessage[]>;

@Injectable({ providedIn: 'root' })
export class ChatOffensiveMessageService {
  public resourceUrl = SERVER_API_URL + 'api/chat-offensive-messages';

  constructor(protected http: HttpClient) {}

  create(chatOffensiveMessage: IChatOffensiveMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatOffensiveMessage);
    return this.http
      .post<IChatOffensiveMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chatOffensiveMessage: IChatOffensiveMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatOffensiveMessage);
    return this.http
      .put<IChatOffensiveMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChatOffensiveMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChatOffensiveMessage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(chatOffensiveMessage: IChatOffensiveMessage): IChatOffensiveMessage {
    const copy: IChatOffensiveMessage = Object.assign({}, chatOffensiveMessage, {
      creationDate:
        chatOffensiveMessage.creationDate != null && chatOffensiveMessage.creationDate.isValid()
          ? chatOffensiveMessage.creationDate.toJSON()
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((chatOffensiveMessage: IChatOffensiveMessage) => {
        chatOffensiveMessage.creationDate = chatOffensiveMessage.creationDate != null ? moment(chatOffensiveMessage.creationDate) : null;
      });
    }
    return res;
  }
}
